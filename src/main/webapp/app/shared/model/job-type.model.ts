import { IContractor } from 'app/shared/model/contractor.model';

export interface IJobType {
  id?: number;
  name?: string;
  contractors?: IContractor[];
}

export class JobType implements IJobType {
  constructor(public id?: number, public name?: string, public contractors?: IContractor[]) {}
}
