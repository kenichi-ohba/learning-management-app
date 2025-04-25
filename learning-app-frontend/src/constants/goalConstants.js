export const GOAL_STATUS = {
  PENDING: 'PENDING',
  IN_PROGRESS: 'IN_PROGRESS',
  COMPLETED: 'COMPLETED',
};

export const GOAL_TYPE = {
  DAILY: 'DAILY',
  WEEKLY: 'WEEKLY',
  MONTHLY: 'MONTHLY',
  LONG_TERM: 'LONG_TERM',
};

// (任意) 日本語表示用のマッピングなど、必要に応じて追加
export const GOAL_STATUS_JP = {
  [GOAL_STATUS.PENDING]: '未達成',
  [GOAL_STATUS.IN_PROGRESS]: '進行中',
  [GOAL_STATUS.COMPLETED]: '達成済',
};

export const GOAL_TYPE_JP = {
  [GOAL_TYPE.DAILY]: '日次',
  [GOAL_TYPE.WEEKLY]: '週次',
  [GOAL_TYPE.MONTHLY]: '月次',
  [GOAL_TYPE.LONG_TERM]: '長期',
};