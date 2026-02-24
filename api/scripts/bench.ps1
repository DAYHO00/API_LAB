param(
  [string]$BaseUrl = "http://localhost:8080",
  [int]$Warmup = 5,
  [int]$Runs = 30
)

function Measure-Endpoint($name, $url) {
  Write-Host "== $name =="
  Write-Host "Warmup $Warmup ..."
  for ($i=0; $i -lt $Warmup; $i++) {
    curl.exe -s $url | Out-Null
  }

  $times = @()
  Write-Host "Run $Runs ..."
  for ($i=0; $i -lt $Runs; $i++) {
    $ms = (Measure-Command { curl.exe -s $url | Out-Null }).TotalMilliseconds
    $times += [math]::Round($ms, 2)
  }

  $sorted = $times | Sort-Object
  $avg = [math]::Round(($times | Measure-Object -Average).Average, 2)
  $p50 = $sorted[[int]($Runs*0.50)]
  $p95 = $sorted[[int]($Runs*0.95)]
  $min = $sorted[0]
  $max = $sorted[$Runs-1]

  return [pscustomobject]@{
    name=$name; avg_ms=$avg; p50_ms=$p50; p95_ms=$p95; min_ms=$min; max_ms=$max
  }
}

# 캐시 히트 상태 만들기 (측정 전 2번)
$cacheUrl = "$BaseUrl/experiments/cache/projects-task-count"
curl.exe -s $cacheUrl | Out-Null
curl.exe -s $cacheUrl | Out-Null

$results = @()
$results += Measure-Endpoint "A_N+1" "$BaseUrl/experiments/nplus1/projects-task-count"
$results += Measure-Endpoint "B_JOIN" "$BaseUrl/experiments/join/projects-task-count"
$results += Measure-Endpoint "C_CACHE_HIT" $cacheUrl

# results 폴더 저장
New-Item -ItemType Directory -Force -Path "results" | Out-Null
$ts = Get-Date -Format "yyyyMMdd-HHmmss"
$out = "results/bench-$ts.json"
$results | ConvertTo-Json -Depth 3 | Out-File -Encoding utf8 $out

$results | Format-Table
Write-Host "Saved: $out"