import { ILease } from 'app/shared/model/lease.model';

export interface IVehicle {
  id?: number;
  make?: string;
  model?: string;
  modelYear?: number;
  licensePlateNumber?: string;
  licensePlateState?: string;
  reservedParkingStallNumber?: string;
  leases?: ILease[];
}

export class Vehicle implements IVehicle {
  constructor(
    public id?: number,
    public make?: string,
    public model?: string,
    public modelYear?: number,
    public licensePlateNumber?: string,
    public licensePlateState?: string,
    public reservedParkingStallNumber?: string,
    public leases?: ILease[]
  ) {}
}
