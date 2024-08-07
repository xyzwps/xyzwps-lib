import { useEffect, useState } from 'react';
import reactLogo from '../assets/react.svg';
import viteLogo from '/vite.svg';
import { getConf } from '../apis/test';

export default function IndexPage() {
  const [count, setCount] = useState(0);
  const [conf, setConf] = useState<Conf | null>(null);
  useEffect(() => {
    getConf().then((confe) => {
      if (confe.type === 'right') {
        setConf(confe.value);
      } else {
        alert(confe.value.message);
      }
    });
  }, []);
  return (
    <div className="p-2">
      <div>
        <a href="https://vitejs.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </div>
      <h1>Vite + React</h1>
      <div className="card">
        <button onClick={() => setCount((count) => count + 1)}>count is {count}</button>
        <p>
          Edit <code>src/App.tsx</code> and save to test HMR
        </p>
      </div>
      <p className="read-the-docs">Click on the Vite and React logos to learn more</p>
      <p>Name: {conf?.name}</p>
    </div>
  );
}
