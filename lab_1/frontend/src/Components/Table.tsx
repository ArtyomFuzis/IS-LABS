import { useState, useEffect } from 'react'
import './App.css'
import {fetchData} from '../utils/apiQueries'

function Table({object : object}) {  

  const [data, setData] = useState([]);

  const loadData = async () => {
      const result = await fetchData();
      setData(result);
  };

  useEffect(() => {
    loadData();
    const interval = setInterval(loadData, 10000);
    return () => clearInterval(interval);
  }, []);

  return (
    <table cellPadding="8" cellSpacing="0">
      <thead>
        <tr>
          <th>ID</th>
          <th>Имя</th>
          <th>Значение</th>
        </tr>
      </thead>
      <tbody>
        {data.map(item => (
          <tr key={item.id}>
            <td>{item.id}</td>
            <td>{item.name}</td>
            <td>{item.value}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default Table