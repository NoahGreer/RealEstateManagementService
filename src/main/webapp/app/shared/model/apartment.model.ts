import { IMaintenance } from 'app/shared/model/maintenance.model';
import { ILease } from 'app/shared/model/lease.model';

export interface IApartment {
  id?: number;
  unitNumber?: string;
  moveInReady?: boolean;
  maintenances?: IMaintenance[];
  leases?: ILease[];
  buildingId?: number;
}

export class Apartment implements IApartment {
  constructor(
    public id?: number,
    public unitNumber?: string,
    public moveInReady?: boolean,
    public maintenances?: IMaintenance[],
    public leases?: ILease[],
    public buildingId?: number
  ) {
    this.moveInReady = this.moveInReady || false;
  }
}
