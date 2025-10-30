import oracledb from "oracledb";
import dotenv from "dotenv";
dotenv.config();

export const dbConfig = {
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  connectString: process.env.DB_CONNECT_STRING,
};

export async function getConnection() {
  return await oracledb.getConnection(dbConfig);
}

export function rowsToObjects(result) {
  const cols = result.metaData.map((m) => m.name.toLowerCase());
  return result.rows.map((r) => {
    const obj = {};
    for (let i = 0; i < cols.length; i++) obj[cols[i]] = r[i];
    return obj;
  });
}
