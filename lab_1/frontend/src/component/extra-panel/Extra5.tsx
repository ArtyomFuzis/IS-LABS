import { useEffect, useState } from "react";
import ParameterField from "../frame/frame-component/ParameterField";
import { processIsValidInt } from "../../util/validations";
import { extra5 } from "../../util/apiQueries";
import ErrorPanel from "../frame/frame-component/ErrorPanel";

function Extras5() {  
  const [x, setX] = useState("")
  const [errorMsgs, setErrorMsg] = useState<string[]>([])
  const [lastState, setlastState] = useState("")

  useEffect(() => {
    let res : string[] = []
    processIsValidInt(x, false, "ID", res)
    setErrorMsg(res)
  }, [x]);

  function createQuery(){
    extra5(x).then(() => {
        setlastState("Операция выполнена успешно!!!")
    }).catch((ret) => {
        setlastState("Ошибка: " + ret.response.data)
    })
  }
  return (
    <>
    <div className="exstras-plate">
        <div className="modal-header">Удаление связь LabWork с Discipline</div>
        <div className="modal-params-container">
            <ParameterField value={x} setValue={setX} required={true} type="intNum" field="ID" />
        </div>
        <div>
            {lastState}
        </div>
        <ErrorPanel errorMessages={errorMsgs} />  
        <button className={`${errorMsgs.length != 0 && "modal-button-disbled"} modal-save-button`} onClick={(createQuery)}>
            Выполнить
        </button>
    </div>
    </>
  );
};

export default Extras5