import Navbar from "../components/Navbar";
import { showToast } from "~/components/ui/toast";
import { createSignal, For, onMount } from "solid-js";
import ProductBidPair from "~/types/ProductBidPair.ts";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "~/components/ui/tabs";
import Spinner from "~/components/Spinner.tsx";
import YourBidCard from "~/components/YourBidCard.tsx";

function YourBids() {
  const [active, setActive] = createSignal<boolean>(true);
  const [productBidPairs, setProductBidPairs] = createSignal<ProductBidPair[]>(
    []
  );

  onMount(async () => {
    const productBidPairs = await fetch(`api/user/bids?active=${active()}`);
    if (productBidPairs.status === 200) {
      console.log(productBidPairs);
      const data = await productBidPairs.json();
      setProductBidPairs(data.data);
    } else if (productBidPairs.status === 500) {
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
      <div class="flex flex-col gap-1 w-9/12 m-auto">
        <span class="text-2xl font-bold">Your Bids</span>
        <Tabs defaultValue="activeBids" class="w-full">
          <TabsList class="grid w-full grid-cols-2 bg-slate-800">
            <TabsTrigger
              value="activeBids"
              onClick={() => {
                setActive(true);
                console.log(productBidPairs());
              }}
            >
              Active Bids
            </TabsTrigger>
            <TabsTrigger
              value="pastBids"
              onClick={() => {
                setActive(false);
                console.log(productBidPairs());
              }}
            >
              Past Bids
            </TabsTrigger>
          </TabsList>
          <TabsContent value="activeBids">
            {productBidPairs().length ? (
              <For each={productBidPairs()} fallback={<Spinner />}>
                {(item) => (
                  <div class="p-2">
                    <YourBidCard
                      productId={item.product.id}
                      description={item.product.description}
                      imgUrl={item.product.media[0]}
                      name={item.product.name}
                      currentBid={item.product.price}
                      yourBid={item.bid.price}
                      bidTime={new Date(item.bid.createdAt)}
                    />
                  </div>
                )}
              </For>
            ) : null}
          </TabsContent>
          <TabsContent value="pastBids">
            {productBidPairs().length ? (
              <For each={productBidPairs()} fallback={<Spinner />}>
                {(item) => (
                  <div class="p-2">
                    <YourBidCard
                      productId={item.product.id}
                      description={item.product.description}
                      imgUrl={item.product.media[0]}
                      name={item.product.name}
                      currentBid={item.product.price}
                      yourBid={item.bid.price}
                      bidTime={new Date(item.bid.createdAt)}
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

export default YourBids;
