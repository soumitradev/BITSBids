import Navbar from "../components/Navbar";
import { showToast } from "~/components/ui/toast";
import { createSignal, For, createEffect } from "solid-js";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "~/components/ui/tabs";
import Spinner from "~/components/Spinner.tsx";
import Product from "~/types/Product";
import YourProductCard from "~/components/YourProductCard";

function YourProducts() {
  const [active, setActive] = createSignal<boolean>(true);
  const [products, setProducts] = createSignal<Product[]>([]);

  createEffect(async () => {
    const products = await fetch(`api/user/products?active=${active()}`);
    if (products.status === 200) {
      const data = await products.json();
      console.log(data);
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
      <div class="flex flex-col gap-1 w-full xl:w-3/4 px-4 m-auto py-4">
        <span class="lg:text-3xl text-2xl font-bold">Your Products</span>
        <Tabs defaultValue="activeProducts" class="w-full">
          <TabsList class="grid w-full grid-cols-2 bg-slate-800">
            <TabsTrigger
              value="activeProducts"
              onClick={() => {
                setActive(true);
                console.log(products());
              }}
            >
              Active Products
            </TabsTrigger>
            <TabsTrigger
              value="pastProducts"
              onClick={() => {
                setActive(false);
                console.log(products());
              }}
            >
              Past Products
            </TabsTrigger>
          </TabsList>
          <TabsContent value="activeProducts">
            {products().length ? (
              <For each={products()} fallback={<Spinner />}>
                {(item) => (
                  <div class="py-2">
                    <YourProductCard
                      productId={item.id}
                      description={item.description}
                      imgUrl={item.media[0]}
                      name={item.name}
                      currentBid={item.price}
                      base_price={item.basePrice}
                      closedAt={new Date(item.closedAt)}
                    />
                  </div>
                )}
              </For>
            ) : null}
          </TabsContent>
          <TabsContent value="pastProducts">
            {products().length ? (
              <For each={products()} fallback={<Spinner />}>
                {(item) => (
                  <div class="py-2">
                    <YourProductCard
                      productId={item.id}
                      description={item.description}
                      imgUrl={item.media[0]}
                      name={item.name}
                      currentBid={item.price}
                      base_price={item.basePrice}
                      closedAt={new Date(item.closedAt)}
                    />
                  </div>
                )}
              </For>
            ) : null}
          </TabsContent>
        </Tabs>
      </div>
    </>
  );
}

export default YourProducts;
