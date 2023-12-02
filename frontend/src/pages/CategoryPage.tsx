import ProductCard from "~/components/ProductCard";
import Navbar from "../components/Navbar";
import { showToast } from "~/components/ui/toast";
import { For, createSignal, onMount } from "solid-js";
import Spinner from "~/components/Spinner";
import Product from "~/types/Product";
import { useParams } from "@solidjs/router";

function CategoryPage() {
  const [products, setProducts] = createSignal<Product[]>([]);
  const params = useParams();

  onMount(async () => {
    const products = await fetch("/api/categories/" + params.id);
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
  });

  return (
    <>
      <Navbar />
      <div class="flex flex-col px-4 pt-4 w-full">
        {products().length ? (
          <div class="w-full overflow-x-auto no-scrollbar mb-6">
            <span class="lg:text-4xl text-3xl font-bold mb-4">Products</span>
            <div class="flex flex-wrap w-full">
              <For each={products()} fallback={<Spinner />}>
                {(item) => (
                  <div class="p-2">
                    <ProductCard
                      closedAt={new Date(item.closedAt)}
                      createdAt={new Date(item.createdAt)}
                      id={item.id}
                      imgUrl={item.media[0]}
                      name={item.name}
                      price={item.price}
                    />
                  </div>
                )}
              </For>
            </div>
          </div>
        ) : null}
      </div>
    </>
  );
}

export default CategoryPage;
