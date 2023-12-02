import { For, Show, createResource, createSignal, onMount } from "solid-js";
import { isServer } from "solid-js/web";
import { As } from "@kobalte/core";
import ShoSho from "shosho";
import { TbCommand, TbHash, TbSearch } from "solid-icons/tb";

import { Badge } from "~/components/ui/badge";
import { Button } from "~/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTrigger,
} from "~/components/ui/dialog";
import { Input } from "~/components/ui/input";
// // @ts-ignore
// const { instantsearch } = window;

const shortcut = new ShoSho();
import type { SearchRes } from "~/types/search";
import { A } from "@solidjs/router";

async function fetchSearch(value: string) {
  const url = `/s/indexes/products/search`;
  const res = await fetch(url, {
    method: "POST",
    body: JSON.stringify({ q: value }),
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
      Authorization:
        "Bearer amFtZXMgamF2YSBvbmNlIHNhaWQgIndyaXRlIG9uY2UsIGdvb2QgbHVjayB3aXRoIHRoYXQgZm9yIHN0YXJ0ZXJzIg==",
    },
  });
  const data: SearchRes = await res.json();
  return data;
}

export default function SearchBar() {
  const [searchValue, setSearchValue] = createSignal("");
  const [isOpen, setIsOpen] = createSignal(false);
  const [data] = createResource(searchValue, fetchSearch);
  // const search = instantsearch({
  //   indexName: "products",
  //   searchClient: instantMeiliSearch(
  //     "http://localhost/s",
  //     "amFtZXMgamF2YSBvbmNlIHNhaWQgIndyaXRlIG9uY2UsIGdvb2QgbHVjayB3aXRoIHRoYXQgZm9yIHN0YXJ0ZXJzIg=="
  //   ),
  // });
  // search.addWidgets([
  //   instantsearch.widgets.searchBox({
  //     container: "#searchbox",
  //   }),
  //   instantsearch.widgets.configure({ hitsPerPage: 8 }),
  //   instantsearch.widgets.hits({
  //     container: "#hits",
  //     templates: {
  //       item: `
  //       <div>
  //       <div class="hit-name">
  //             {{#helpers.highlight}}{ "attribute": "title" }{{/helpers.highlight}}
  //       </div>
  //       </div>
  //     `,
  //     },
  //   }),
  // ]);
  // search.start();
  data();

  onMount(() => {
    if (!isServer) {
      shortcut.register("CmdOrCtrl+K", () => {
        document.getElementById("search-trigger")?.click();
        return true;
      });
      shortcut.start();
    }
  });

  return (
    <Dialog onOpenChange={setIsOpen} open={isOpen()}>
      <DialogTrigger asChild class="mr-4 hidden sm:flex">
        <As
          component={Button}
          id="search-trigger"
          variant="outline"
          class="text-muted-foreground relative w-full justify-between space-x-4"
        >
          <div class="flex items-center space-x-2">
            <TbSearch />
            <span>Search...</span>
          </div>

          <Badge variant="secondary" class="flex items-center ">
            <TbCommand size={16} />
            <span>K</span>
          </Badge>
        </As>
      </DialogTrigger>
      <DialogTrigger
        asChild
        class="mr-2 md:w-10 md:h-10 h-8 w-8 p-0 sm:hidden flex"
      >
        <As
          component={Button}
          variant="secondary"
          class="flex rounded-full p-0 px-3"
        >
          <TbSearch />
        </As>
      </DialogTrigger>

      <DialogContent class="flex h-[80%] flex-col">
        <DialogHeader class="mx-4">
          <Input
            placeholder="Search..."
            value={searchValue()}
            onkeyup={(v) => setSearchValue(v.currentTarget.value)}
          />
        </DialogHeader>

        <DialogDescription>
          <Show
            when={!data.loading}
            fallback={<p class="text-center">Loading...</p>}
          >
            <For each={data()?.hits}>
              {(item) => {
                return (
                  <A
                    href={"/products/" + item.id}
                    onclick={() => setIsOpen(false)}
                    class="duration-400 animate-in fade-in-0"
                  >
                    <div class="grid grid-cols-[25px_1fr] items-start rounded p-4 hover:bg-black/30 dark:hover:bg-white/30">
                      <TbHash size={20} />
                      <div class="space-y-1">
                        <p class="text-base font-medium leading-none text-slate-50">
                          {item.name}
                        </p>
                        <p class="text-muted-foreground text-sm"></p>
                        {item.description}
                      </div>
                    </div>
                  </A>
                );
              }}
            </For>
          </Show>
        </DialogDescription>
      </DialogContent>
    </Dialog>
  );
}
