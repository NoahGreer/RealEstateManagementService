import { ILease } from 'app/shared/model/lease.model';

export interface IPet {
  id?: number;
  name?: string;
  type?: string;
  color?: string;
  certifiedAssistanceAnimal?: boolean;
  leases?: ILease[];
}

export class Pet implements IPet {
  constructor(
    public id?: number,
    public name?: string,
    public type?: string,
    public color?: string,
    public certifiedAssistanceAnimal?: boolean,
    public leases?: ILease[]
  ) {
    this.certifiedAssistanceAnimal = this.certifiedAssistanceAnimal || false;
  }
}
