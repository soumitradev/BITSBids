import ProductCard from "~/components/ProductCard";
import Navbar from "../components/Navbar";
import { showToast } from "~/components/ui/toast";
import { For, createSignal, onMount } from "solid-js";
import Spinner from "~/components/Spinner";
import CategoryCard from "~/components/CategoryCard";
import Product from "~/types/Product";
import ProductBidPair from "~/types/ProductBidPair";

function Home() {
  const [products, setProducts] = createSignal<Product[]>([]);
  const [categories, setCategories] = createSignal<Category[]>([]);
  const [bidProducts, setBidProducts] = createSignal<Product[]>([]);

  onMount(async () => {
    const products = await fetch("/api/product/latest");
    if (products.status === 200) {
      const data = await products.json();
      setProducts(data.data);
    } else if (products.status === 500) {
      showToast({
        title: "Server Error",
        description: "The server might be down",
        variant: "destructive",
      });
    }

    const bidProducts = await fetch("/api/user/bids?active=true");
    if (bidProducts.status === 200) {
      const data = await bidProducts.json();
      setBidProducts(data.data.map((pair: ProductBidPair) => pair.product));
    } else if (bidProducts.status === 500) {
      showToast({
        title: "Server Error",
        description: "The server might be down",
        variant: "destructive",
      });
    }

    const categories = await fetch("/api/categories");
    if (categories.status === 200) {
      const data = await categories.json();
      setCategories(data.data);
    } else if (categories.status === 500) {
      showToast({
        title: "Server Error",
        description: "The server might be down",
        variant: "destructive",
      });
    }
  });

  return (
    <>
      <Navbar />
      <div class="flex flex-col px-4 pt-4 w-full">
        {bidProducts().length ? (
          <div class="w-full overflow-x-auto no-scrollbar mb-6">
            <span class="lg:text-3xl text-2xl font-bold">Active Bids</span>

            <div class="flex space-x-2 pt-2 w-fit">
              <For each={bidProducts()} fallback={<Spinner />}>
                {(item) => (
                  <ProductCard
                    closedAt={new Date(item.closedAt)}
                    createdAt={new Date(item.createdAt)}
                    id={item.id}
                    imgUrl={item.media[0]}
                    name={item.name}
                    price={item.price}
                  />
                )}
              </For>
            </div>
          </div>
        ) : null}
        {products().length ? (
          <div class="w-full overflow-x-auto no-scrollbar mb-6">
            <span class="lg:text-3xl text-2xl font-bold">Latest Products</span>
            <div class="flex space-x-2 pt-2 w-fit">
              <For each={products()} fallback={<Spinner />}>
                {(item) => (
                  <ProductCard
                    closedAt={new Date(item.closedAt)}
                    createdAt={new Date(item.createdAt)}
                    id={item.id}
                    imgUrl={item.media[0]}
                    name={item.name}
                    price={item.price}
                  />
                )}
              </For>
            </div>
          </div>
        ) : null}
        {categories().length ? (
          <div class="w-full overflow-x-auto no-scrollbar mb-6">
            <span class="lg:text-3xl text-2xl font-bold">Categories</span>
            <div class="flex space-x-2 pt-2 w-fit">
              <For each={categories()} fallback={<Spinner />}>
                {(item) => (
                  <CategoryCard
                    id={item.id}
                    name={item.name}
                    imgUrl={"http://localhost/f/6,17c83af8bd"}
                  />
                )}
              </For>
            </div>
          </div>
        ) : null}
      </div>
    </>
  );
}

export default Home;
