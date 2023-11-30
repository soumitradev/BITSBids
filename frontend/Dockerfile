FROM node:18.16.0-slim AS build-stage
ENV NODE_ENV production
WORKDIR /usr/src/app

ENV PNPM_HOME="/root/.local/share/pnpm"
ENV PATH="${PATH}:${PNPM_HOME}"

COPY --chown=node:node tsconfig.json ./
COPY --chown=node:node tsconfig.node.json ./
COPY --chown=node:node .env* ./
COPY --chown=node:node public ./
COPY --chown=node:node postcss.config.cjs ./
COPY --chown=node:node tailwind.config.cjs ./
COPY --chown=node:node vite.config.ts ./
COPY --chown=node:node ui.config.json ./
COPY --chown=node:node ui.preset.js ./
COPY --chown=node:node index.html ./

RUN npm install -g pnpm

COPY package.json ./
COPY pnpm-lock.yaml ./

RUN pnpm fetch --prod
RUN pnpm install -r --offline --prod

COPY --chown=node:node src ./src

RUN pnpm run build

FROM nginx:1.25.3-alpine-slim
WORKDIR /usr/share/nginx/html

RUN mkdir /usr/log
RUN rm -rf ./*
RUN rm -rf /etc/nginx/.htpasswd
COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=build-stage /usr/src/app/dist .

CMD ["nginx", "-g", "daemon off;"]