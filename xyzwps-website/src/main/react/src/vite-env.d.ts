/// <reference types="vite/client" />

type Either<E, V> = { type: 'left'; value: E } | { type: 'right'; value: V };

type Conf = { name: string };
