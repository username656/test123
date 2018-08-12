import { environment as defaultEnvironment } from './environment';

const time = new Date().getTime();
export const environmentLoader = new Promise<{ [key: string]: string | boolean }>((resolve) => {
  const xmlhttp = new XMLHttpRequest(),
    method = 'GET',
    url = './assets/environment.json?' + time;
  xmlhttp.open(method, url, true);
  xmlhttp.onload = function(): void {
    if (xmlhttp.status === 200) {
      resolve(JSON.parse(xmlhttp.responseText));
    } else {
      resolve(defaultEnvironment);
    }
  };
  xmlhttp.send();
});
