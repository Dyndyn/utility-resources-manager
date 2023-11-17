export interface IUtility {
  id: number;
  name?: string | null;
  constant?: boolean | null;
  predictable?: boolean | null;
}

export type NewUtility = Omit<IUtility, 'id'> & { id: null };
