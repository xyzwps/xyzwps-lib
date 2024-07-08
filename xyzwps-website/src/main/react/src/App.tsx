import { Outlet, RouterProvider, Link, createRouter, createRoute, createRootRoute } from '@tanstack/react-router';
import { TanStackRouterDevtools } from '@tanstack/router-devtools';
import IndexPage from './pages/IndexPage';
import AboutPage from './pages/AboutPage';
import SimpleRegisterPage from './pages/register/SimpleRegisterPage';
import './App.css';

const rootRoute = createRootRoute({
  component: () => (
    <>
      <div>
        <Link to="/">Home</Link> <Link to="/about">About</Link>
      </div>
      <hr />
      <Outlet />
      <TanStackRouterDevtools />
    </>
  ),
});

const indexRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: '/',
  component: IndexPage,
});

const aboutRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: '/about',
  component: AboutPage,
});

const simpleResigerRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: '/register/simple',
  component: SimpleRegisterPage,
});

const routeTree = rootRoute.addChildren([indexRoute, aboutRoute, simpleResigerRoute]);

const router = createRouter({ routeTree });

declare module '@tanstack/react-router' {
  interface Register {
    router: typeof router;
  }
}

export default function App() {
  return <RouterProvider router={router} />;
}
