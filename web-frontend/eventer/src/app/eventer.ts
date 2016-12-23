export class Eventer {
  externalLink: string = '';
  description: string = '';
  name: string = '';
  owner: string = '';
  date: '';

  constructor(values: Object  = {}) {
    Object.assign(this,values);
  }
}
