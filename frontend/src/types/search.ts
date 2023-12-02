export interface SearchRes {
  hits: Document[];
}
export interface Document {
  id: string;
  name: string;
  description: string;
}
