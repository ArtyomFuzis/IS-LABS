import { useEffect, useState } from "react";
import ParameterField from "../frame/frame-component/ParameterField";
import { processIsValidInt } from "../../util/validations";
import { extra4 } from "../../util/apiQueries";
import ErrorPanel from "../frame/frame-component/ErrorPanel";

function Extras4() {  
  const [id, setID] = useState("")
  const [steps, setSteps] = useState("")
  const [errorMsgs, setErrorMsg] = useState<string[]>([])
  const [lastState, setlastState] = useState("")

  useEffect(() => {
    let res : string[] = []
    processIsValidInt(id, false, "ID", res)
    processIsValidInt(steps, false, "Steps", res)
    setErrorMsg(res)
  }, [id, steps]);

  function createQuery(){
    extra4(id, steps).then((ret) => {
        if(ret.success){
          setlastState("Операция выполнена успешно!!!")
        } else {
          setlastState("Операция не была выполнена усепешно, проверьте, что steps не выходит за допустимые границы!!!")
        }
    }).catch((ret) => {
        setlastState("Ошибка: " + ret.response.data)
    })
  }
  return (
    <>
    <div className="exstras-plate">
        <div className="modal-header">Добавление сложности LabWork</div>
        <div className="modal-params-container">
            <ParameterField value={id} setValue={setID} required={true} type="intNum" field="ID" />
            <ParameterField value={steps} setValue={setSteps} required={true} type="intNum" field="Steps" />
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

export default Extras4