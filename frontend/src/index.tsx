import "./styles/global.css";

import { render } from "solid-js/web";
import {
  ColorModeProvider,
  ColorModeScript,
  createLocalStorageManager,
} from "@kobalte/core";
import { Route, Router, Routes } from "@solidjs/router";
import Login from "./pages/Login";
import Home from "./pages/Home";
import { Toaster } from "~/components/ui/toast";
import UserDetails from "./pages/UserDetails";
import Categories from "./pages/Categories";
import UserProfile from "~/pages/UserProfile.tsx";

import YourBids from "~/pages/YourBids.tsx";
import YourProducts from "./pages/YourProducts";
import CategoryPage from "./pages/CategoryPage";

const storageManager = createLocalStorageManager(document.cookie);

render(
  () => (
    <>
      <ColorModeScript storageType={storageManager.type} />
      <ColorModeProvider storageManager={storageManager}>
        <Toaster />
        <Router>
          <Routes>
            <Route path="/" component={Login} />
            <Route path="/home" component={Home} />
            <Route path="/details" component={UserDetails} />
            <Route path="/categories" component={Categories} />
            <Route path="/categories/:id" component={CategoryPage} />
            <Route path="/bids" component={YourBids} />
            <Route path="/products" component={YourProducts} />
            <Route path="/profile" component={UserProfile} />
          </Routes>
        </Router>
      </ColorModeProvider>
    </>
  ),
  document.getElementById("root")!
);
