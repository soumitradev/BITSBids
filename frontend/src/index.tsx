import "./styles/global.css";

import { render } from "solid-js/web";
import {
  ColorModeProvider,
  ColorModeScript,
  createLocalStorageManager,
} from "@kobalte/core";
const storageManager = createLocalStorageManager(document.cookie);
import { Router, Route, Routes } from "@solidjs/router";
import Login from "./pages/Login";
import Home from "./pages/Home";
import { Toaster } from "~/components/ui/toast";
import UserDetails from "./pages/UserDetails";
import Categories from "./pages/Categories";

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
          </Routes>
        </Router>
      </ColorModeProvider>
    </>
  ),
  document.getElementById("root")!
);
