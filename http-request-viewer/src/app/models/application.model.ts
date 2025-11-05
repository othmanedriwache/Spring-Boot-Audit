export interface Application {
  id: string;
  name: string;
  version: string;
  creationDate: string;
  applicationInstances?: ApplicationInstance[];
}

export interface ApplicationInstance {
  id: string;
  creationDate: string;
}

export interface ApplicationWithInstances {
  id: string;
  name: string;
  version: string;
  creationDate: string;
  applicationInstances: ApplicationInstance[];
}
