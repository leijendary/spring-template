import { uuidv4 } from "https://jslib.k6.io/k6-utils/1.4.0/index.js";
import { check } from "k6";
import http from "k6/http";

const url = "http://localhost:30080";

export const options = {
  stages: [
    {
      duration: "20s",
      target: 100,
    },
    {
      duration: "5m",
      target: 500,
    },
  ],
};

export default function () {
  const id = create();
  get(id);
  update(id);
  remove(id);
}

function create() {
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
        description: `Testing the japanese description ${uuid}`,
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

  const json = res.json();

  return json.id;
}

function get(id) {
  const params = {
    headers: {
      "Content-Type": "application/json",
    },
  };
  const res = http.get(`${url}/api/v1/samples/admin/${id}`, params);

  check(res, {
    "get status is 200": (r) => r.status === 200,
  });
}

function update(id) {
  const payload = JSON.stringify({
    name: `Test updated ${id}`,
    description: `I am testing updated record ${id}`,
    amount: 9999999.99,
    translations: [
      {
        name: `Test dutch updated ${id}`,
        description: `Testing the dutch description ${id}`,
        language: "nl",
        ordinal: 1,
      },
      {
        name: `Test english updated ${id}`,
        language: "en",
        ordinal: 2,
      },
      {
        name: `Test japanese updated ${id}`,
        description: `Testing the japanese description updated ${id}`,
        language: "jp",
        ordinal: 3,
      },
    ],
  });
  const params = {
    headers: {
      "Content-Type": "application/json",
    },
  };
  const res = http.put(`${url}/api/v1/samples/admin/${id}?version=0`, payload, params);

  check(res, {
    "update status is 200": (r) => r.status === 200,
  });
}

function remove(id) {
  const res = http.del(`${url}/api/v1/samples/admin/${id}?version=1`);

  check(res, {
    "update status is 204": (r) => r.status === 204,
  });
}
