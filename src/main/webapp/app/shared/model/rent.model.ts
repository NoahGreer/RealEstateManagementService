import { Moment } from 'moment';

export interface IRent {
  id?: number;
  dueDate?: Moment;
  recievedDate?: Moment;
  amount?: number;
  leaseId?: number;
}

export class Rent implements IRent {
  constructor(public id?: number, public dueDate?: Moment, public recievedDate?: Moment, public amount?: number, public leaseId?: number) {}
}
