import "./styles/global.css";

import { render } from "solid-js/web";
import {
  ColorModeProvider,
  ColorModeScript,
  createLocalStorageManager,
} from "@kobalte/core";
const storageManager = createLocalStorageManager(document.cookie);
import { Router, Route, Routes } from "@solidjs/router";
import Login from "./Login";
import Home from "./Home";
import { Toaster } from "~/components/ui/toast";

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
          </Routes>
        </Router>
      </ColorModeProvider>
    </>
  ),
  document.getElementById("root")!
);
