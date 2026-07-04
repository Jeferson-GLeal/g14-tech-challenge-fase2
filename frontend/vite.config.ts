import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

const apiTarget = process.env.VITE_API_PROXY_TARGET ?? 'http://localhost:8080';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    strictPort: true,
    proxy: {
      '/api': {
        target: apiTarget,
        changeOrigin: true,
      },
      '/restaurants': {
        target: apiTarget,
        changeOrigin: true,
      },
      '/v3': {
        target: apiTarget,
        changeOrigin: true,
      },
      '/swagger-ui': {
        target: apiTarget,
        changeOrigin: true,
      },
    },
  },
});
