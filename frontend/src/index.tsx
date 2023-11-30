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
          </Routes>
        </Router>
      </ColorModeProvider>
    </>
  ),
  document.getElementById("root")!
);
