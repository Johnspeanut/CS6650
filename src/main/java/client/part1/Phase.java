package client.part1;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class Phase {
  public ExecutorService threadsPool;
  private CountDownLatch latch;
  private CountDownLatch tenPercentLatch;
  private int numThreads;
  private int numRuns;
  private int numSkiers;
  private int startTime;
  private int endTime;
  private int numLifts;
  private int numRequests;
  private int totalRequests;
  private int totalFailedCount;
  private ConcurrentHashMap<String, RequestRecord> records = new ConcurrentHashMap<String, RequestRecord>();
  private String basePath;
  private boolean traceLatency;

  public Phase(int numThreads, int numRuns, int numSkiers, int startTime, int endTime, int numLifts,
      String basePath, boolean traceLatency) {
    this.numThreads = numThreads;
    this.numRuns = numRuns;
    this.numSkiers = numSkiers;
    this.startTime = startTime;
    this.endTime = endTime;
    this.numLifts = numLifts;
    this.basePath = basePath;
    this.traceLatency = traceLatency;

    this.numRequests = this.numRuns*this.numSkiers/this.numThreads;
    this.threadsPool = Executors.newFixedThreadPool(numThreads);
    this.latch = new CountDownLatch(numThreads);
    this.tenPercentLatch = new CountDownLatch(numThreads/10);
  }

  public void addRecord(long startTime, String requestType, long latency, int responseCode){
    this.records.put(UUID.randomUUID().toString(), new RequestRecord(startTime, requestType,latency,responseCode));
  }

  public synchronized void failCount(){
    this.totalFailedCount++;
  }

  public synchronized void totalCount(){
    this.totalRequests++;
  }

  public ConcurrentHashMap<String, RequestRecord> getRecords(){
    return this.records;
  }

  public int getTotalRequests() {
    return totalRequests;
  }

  public int getTotalFailedCount() {
    return totalFailedCount;
  }

  public void start(){
    for(int i = 0; i < this.numThreads; i++){
      final int[] boundaries = getBoundaries(numSkiers, numThreads, i);
      Runnable runner = ()->{
        skiersAPi apiInstance = new SkiersApi();
        ApiClient client = apiInstance.getApiClient();
        client.setBasePath(this.basePath);

        for(int j = 0; j < this.numRequests; j++){
          LiftRide liftRide = new LiftRide();
          int startTime = ThreadLocalRandom.current().nextInt(this.startTime, this.endTime + 1);
          liftRide.setTime(startTime);
          liftRide.setLiftID(ThreadLocalRandom.current().nextInt(this.numLifts) + 1);
          int skierId = ThreadLocalRandom.current().nextInt(boundaries[0], boundaries[1] + 1);
          long positStartTime = 0;
          if(this.traceLatency){
            positStartTime = System.currentTimeMillis();
          }
          int resonseCode = 201;
          try{
            ApiResponse response = apiInstance.writeNewLiftRideWithHttpInfo(liftRide, 1, "2019", "1", skierId);
            resonseCode = response.getStatusCode();
          }catch (ApiException e){
            this.failCount();
            resonseCode = e.getCode();
          }finally {
            if(this.traceLatency){
              addRecord(postStartTime, "post", System.currentTimeMillis() - positStartTime, resonseCode);
            }
            this.totalCount();
          }
        }
        this.tenPercentLatch.countDown();
        this.latch.countDown();
      };
      this.threadsPool.submit(runner);

    }


  }

  public void waitForTenPercent(){
    try{
      this.tenPercentLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void waitForCompletion(){
    try{
      this.latch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private int[] getBoundaries(int numSkiers, int numThreads, int rank){
    int[] res = new int[2];
    int numGroup = numSkiers / numThreads;
    res[0] = rank * numGroup + 1;
    res[1] = res[0] + numGroup - 1;
    if(rank == numThreads - 1){
      res[1] = numSkiers;
    }
    return res;
  }
}
