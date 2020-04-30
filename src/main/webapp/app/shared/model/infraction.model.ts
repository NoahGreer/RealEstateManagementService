import { Moment } from 'moment';

export interface IInfraction {
  id?: number;
  dateOccurred?: Moment;
  cause?: string;
  resolution?: string;
  leaseId?: number;
}

export class Infraction implements IInfraction {
  constructor(
    public id?: number,
    public dateOccurred?: Moment,
    public cause?: string,
    public resolution?: string,
    public leaseId?: number
  ) {}
}
