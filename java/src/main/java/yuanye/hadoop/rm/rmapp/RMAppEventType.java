package yuanye.hadoop.rm.rmapp;

public enum RMAppEventType {
  // Source: ClientRMService
  START,
  RECOVER,
  KILL,

  // Source: RMAppAttempt
  APP_REJECTED,
  APP_ACCEPTED,
  ATTEMPT_REGISTERED,
  ATTEMPT_UNREGISTERED,
  ATTEMPT_FINISHED, // Will send the final state
  ATTEMPT_FAILED,
  ATTEMPT_KILLED,
  NODE_UPDATE,

  // Source: RMStateStore
  APP_SAVED,
  APP_REMOVED
}