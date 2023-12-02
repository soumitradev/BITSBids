import Product from "./Product";
import Bid from "~/types/Bid.ts";

export default interface ProductBidPair {
  product: Product;
  bid: Bid
}
