import { useEffect, useState } from "react";
import ParameterField from "../frame/frame-component/ParameterField";
import { processIsPositive, processIsValidInt } from "../../util/validations";
import { extra1 } from "../../util/apiQueries";
import ErrorPanel from "../frame/frame-component/ErrorPanel";

function Extras1() {  
  const [x, setX] = useState("")
  const [errorMsgs, setErrorMsg] = useState<string[]>([])
  const [lastState, setlastState] = useState("")

  useEffect(() => {
    let res : string[] = []
    processIsValidInt(x, false, "authorID", res)
    processIsPositive(x, false, "authorID", res)
    setErrorMsg(res)
  }, [x]);

  function createQuery(){
    extra1(x).then(() => {
        setlastState("Операция выполнена успешно!!!")
    }).catch((ret) => {
        setlastState("Ошибка: нет автора с таким ID")
    })
  }
  return (
    <>
    <div className="exstras-plate">
        <div className="modal-header">Удаление LabWork по authorId</div>
        <div className="modal-params-container">
            <ParameterField value={x} setValue={setX} required={true} type="intNum" field="authorID" />
        </div>
        <div>
            {lastState}
        </div>
        {(x !="") && <ErrorPanel errorMessages={errorMsgs} /> } 
        <button className={`${errorMsgs.length != 0 && "modal-button-disbled"} modal-save-button`} onClick={(createQuery)}>
            Выполнить
        </button>
    </div>
    </>
  );
};

export default Extras1