import { defineConfig, loadEnv } from 'vite';
import path from 'path';
import vue from '@vitejs/plugin-vue';
import vueJsx from '@vitejs/plugin-vue-jsx';
import envCompatible from 'vite-plugin-env-compatible';
import { createHtmlPlugin } from 'vite-plugin-html';
import { viteCommonjs } from '@originjs/vite-plugin-commonjs';

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  // 환경 변수 로드
  const env = loadEnv(mode, process.cwd(), '');

  return {
    define: {
      __VUE_OPTIONS_API__: true, // 필요한 플래그 설정
      __VUE_PROD_DEVTOOLS__: false,
    },
    plugins: [
      vue(),
      vueJsx(),
      viteCommonjs(),
      envCompatible(),
      createHtmlPlugin({
        inject: {
          data: {
            title: 'frontend',
          },
        },
      }),
    ],
    resolve: {
      alias: [
        {
          find: /^~/,
          replacement: '',
        },
        {
          find: '@',
          replacement: path.resolve(__dirname, 'src'),
        },
      ],
      extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue'],
    },
    server: {
      proxy: {
        '/api': {
          target: env.VITE_API_TARGET,
          changeOrigin: true,
          // secure: false,
        },
        '/ws': {
          target: env.VITE_WEBSOCKET_URL,
          ws: true,
          changeOrigin: true,
        },
        // '/api/exchange': {
        //   target: 'https://v6.exchangerate-api.com/v6/6bbbf78cc42a296d533a9e6b',
        //   changeOrigin: true,
        //   rewrite: (path) => path.replace(/^\/api\/exchange/, ''),
        // },
      },
    },
    build: {},
  };
});