import { useState } from "react";
import { extra2 } from "../../util/apiQueries";

function Extras2() {  
  const [lastState, setlastState] = useState("")


  function createQuery(){
    extra2().then((res) => {
        setlastState("Ответ: " + res.result)
    }).catch((ret) => {
        setlastState("Ошибка: " + ret.response.data)
    })
  }
  return (
    <>
    <div className="exstras-plate">
        <div className="modal-header">Сумма значений maximumPoint для всех объектов </div>
        <div className="exstras-field"></div>
        <div>
            {lastState}
        </div>
        <button className={`modal-save-button`} onClick={(createQuery)}>
            Выполнить
        </button>
    </div>
    </>
  );
};

export default Extras2