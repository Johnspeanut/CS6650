package client.part1;

public class ClientPart1 {
  // Maximum number of threads to run (numThreads: max 256)
  private int numThreads;
  // Number of skier to generate lift rides (numSkiers:max 50000). Skier's ID range [0, numSkiers)
  private int numSkiers;
  // Number of lifts (range 5-60, default 40)
  private int numLifts = 40;
  // Mean number of ski lifts each skier rides per day( numRuns: default 10, max 20)
  private int numRuns = 10;
  // IP and port of the server
  private String basePath;
  private Phase phase1;
  private Phase phase2;
  private Phase phase3;
  private long wallTime;
  private boolean traceLatency;

  public ClientPart1(int numThreads, int numSkiers, int numLifts, int numRuns,
      String basePath, Phase phase1, Phase phase2, Phase phase3,
      boolean traceLatency) {
    this.numThreads = Math.min(256,numThreads);
    this.numSkiers = Math.min(50000,numSkiers);
    this.numLifts = Math.min(Math.max(numLifts,5),60);
    this.numRuns = Math.min(20,numRuns);
    this.basePath = basePath;
    this.traceLatency = traceLatency;

    // Define phases
    this.phase1 = new Phase(this.numThreads/4, this.numRuns/10, this.numSkiers,0,90,this.numLifts,basePath,this.traceLatency);
    this.phase2 = new Phase(this.numThreads/4, this.numRuns*8/10, this.numSkiers,91,360,this.numLifts,basePath,this.traceLatency);
    this.phase3 = new Phase(this.numThreads/4, this.numRuns/10, this.numSkiers,361,420,this.numLifts,basePath,this.traceLatency);
  }
}
