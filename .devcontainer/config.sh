sudo chown -R vscode:vscode /tmp/cache/pnpm
pnpm config set store-dir /tmp/cache/pnpm
pnpm install --prefer-offline --ignore-scripts