import { Moment } from 'moment';
import { IRent } from 'app/shared/model/rent.model';
import { IInfraction } from 'app/shared/model/infraction.model';
import { IPerson } from 'app/shared/model/person.model';
import { IVehicle } from 'app/shared/model/vehicle.model';
import { IPet } from 'app/shared/model/pet.model';

export interface ILease {
  id?: number;
  dateSigned?: Moment;
  moveInDate?: Moment;
  noticeOfRemovalOrMoveoutDate?: Moment;
  endDate?: Moment;
  amount?: number;
  leaseType?: string;
  notes?: string;
  rents?: IRent[];
  infractions?: IInfraction[];
  newLeaseId?: number;
  people?: IPerson[];
  vehicles?: IVehicle[];
  pets?: IPet[];
  apartmentId?: number;
}

export class Lease implements ILease {
  constructor(
    public id?: number,
    public dateSigned?: Moment,
    public moveInDate?: Moment,
    public noticeOfRemovalOrMoveoutDate?: Moment,
    public endDate?: Moment,
    public amount?: number,
    public leaseType?: string,
    public notes?: string,
    public rents?: IRent[],
    public infractions?: IInfraction[],
    public newLeaseId?: number,
    public people?: IPerson[],
    public vehicles?: IVehicle[],
    public pets?: IPet[],
    public apartmentId?: number
  ) {}
}
