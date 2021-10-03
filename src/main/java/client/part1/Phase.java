package client.part1;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    }

  }
}
