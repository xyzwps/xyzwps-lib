import ky from 'ky';

export const getConf = async (): Promise<Either<Error, Conf>> => {
  try {
    const body = await ky.get('/api/test/conf').json();
    return { type: 'right', value: body as Conf };
  } catch (e) {
    if (e instanceof Error) {
      return { type: 'left', value: e };
    } else {
      return { type: 'left', value: new Error('Unknown error') };
    }
  }
};
