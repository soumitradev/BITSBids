import Navbar from "../components/Navbar";
import { showToast } from "~/components/ui/toast";
import { For, createSignal, onMount } from "solid-js";
import Spinner from "~/components/Spinner";
import CategoryCard from "~/components/CategoryCard";

function Categories() {
  const [categories, setCategories] = createSignal<Category[]>([]);

  onMount(async () => {
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
      <div class="flex flex-col px-12 pt-4 w-full">
        {categories().length ? (
          <div class="w-full overflow-x-auto no-scrollbar mb-6">
            <span class="lg:text-4xl text-3xl font-bold mb-4">Categories</span>
            <div class="flex flex-wrap w-full">
              <For each={categories()} fallback={<Spinner />}>
                {(item) => (
                  <div class="p-2">
                    <CategoryCard
                      id={item.id}
                      name={item.name}
                      imgUrl={"http://localhost/f/6,17c83af8bd"}
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

export default Categories;
