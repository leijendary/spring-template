import { uuidv4 } from "https://jslib.k6.io/k6-utils/1.4.0/index.js";
import { check } from "k6";
import http from "k6/http";

const url = "http://localhost:82";

export const options = {
  stages: [
    {
      duration: "20s",
      target: 100,
    },
    {
      duration: "2m",
      target: 100,
    }
  ],
};

export default function () {
  const uuid = uuidv4();
  const payload = JSON.stringify({
    name: `Test ${uuid}`,
    description: `I am testing record ID ${uuid}`,
    amount: 222.25,
    translations: [
      {
        name: `Test english ${uuid}`,
        language: "en",
        ordinal: 1,
      },
      {
        name: `Test japanese ${uuid}`,
        description: `Testing the jaapanese description ${uuid}`,
        language: "jp",
        ordinal: 2,
      },
    ],
  });
  const params = {
    headers: {
      "Content-Type": "application/json",
    },
  };
  const res = http.post(`${url}/api/v1/samples/admin`, payload, params);

  check(res, {
    "create status is 201": (r) => r.status === 201,
  });
}
