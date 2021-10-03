package client.part1;

public class RequestRecord {
  private long startTime;
  private String requestType;
  private long latency;
  private int responseCode;

  public RequestRecord(long startTime, String requestType, long latency, int responseCode) {
    this.startTime = startTime;
    this.requestType = requestType;
    this.latency = latency;
    this.responseCode = responseCode;
  }

  public long getLatency() {
    return latency;
  }

  @Override
  public String toString() {
    return "RequestRecord{" +
        "startTime=" + startTime +
        ", requestType='" + requestType + '\'' +
        ", latency=" + latency +
        ", responseCode=" + responseCode +
        '}';
  }
}
