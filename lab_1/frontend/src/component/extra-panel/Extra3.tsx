import { useState } from "react";
import { extra3 } from "../../util/apiQueries";

function Extras3() {  
  const [lastState, setlastState] = useState("")


  function createQuery(){
    extra3().then((res) => {
        setlastState("Ответ: " + res.result)
    }).catch((ret) => {
        setlastState("Ошибка: " + ret.response.data)
    })
  }
  return (
    <>
    <div className="exstras-plate">
        <div className="modal-header">Уникальные значения minimalPoint для всех объектов </div>
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

export default Extras3