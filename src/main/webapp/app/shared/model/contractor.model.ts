import { IMaintenance } from 'app/shared/model/maintenance.model';
import { IJobType } from 'app/shared/model/job-type.model';

export interface IContractor {
  id?: number;
  companyName?: string;
  streetAddress?: string;
  city?: string;
  stateCode?: string;
  zipCode?: string;
  phoneNumber?: string;
  contactPerson?: string;
  ratingOfWork?: number;
  ratingOfResponsiveness?: number;
  maintenances?: IMaintenance[];
  jobTypes?: IJobType[];
}

export class Contractor implements IContractor {
  constructor(
    public id?: number,
    public companyName?: string,
    public streetAddress?: string,
    public city?: string,
    public stateCode?: string,
    public zipCode?: string,
    public phoneNumber?: string,
    public contactPerson?: string,
    public ratingOfWork?: number,
    public ratingOfResponsiveness?: number,
    public maintenances?: IMaintenance[],
    public jobTypes?: IJobType[]
  ) {}
}
