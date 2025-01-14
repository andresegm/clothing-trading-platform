export interface Trade {
  id: number;
  status: string;
  itemId: number;
  itemTitle: string;
  initiatorId: number;
  initiatorUsername: string;
  receiverId: number;
  receiverUsername: string;
}
