export default interface Product {
  name: string;
  description: string;
  id: number;
  price: number;
  media: string[];
  autoSellPrice: number;
  basePrice: number;
  closedAt: Date;
  createdAt: Date;
}
