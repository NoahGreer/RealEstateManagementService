import { Moment } from 'moment';
import { ILease } from 'app/shared/model/lease.model';

export interface IPerson {
  id?: number;
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
  emailAddress?: string;
  primaryContact?: boolean;
  isMinor?: boolean;
  ssn?: string;
  backgroundCheckDate?: Moment;
  backgroundCheckConfirmationNumber?: string;
  employmentVerificationDate?: Moment;
  employmentVerificationConfirmationNumber?: string;
  leases?: ILease[];
}

export class Person implements IPerson {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public phoneNumber?: string,
    public emailAddress?: string,
    public primaryContact?: boolean,
    public isMinor?: boolean,
    public ssn?: string,
    public backgroundCheckDate?: Moment,
    public backgroundCheckConfirmationNumber?: string,
    public employmentVerificationDate?: Moment,
    public employmentVerificationConfirmationNumber?: string,
    public leases?: ILease[]
  ) {
    this.primaryContact = this.primaryContact || false;
    this.isMinor = this.isMinor || false;
  }
}
