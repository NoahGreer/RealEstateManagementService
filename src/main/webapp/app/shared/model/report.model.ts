export interface IReport {
  id?: number;
  name?: string;
  fields?: string[];
}

export class Report implements IReport {
  constructor(public id?: number, public name?: string, public fields?: string[]) {}
}
