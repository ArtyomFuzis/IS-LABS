import { useEffect, useState } from "react"
import ParameterField from "../FrameComponents/ParameterField"
import { processIsValidFloat, processIsLowerThan, processIsBiggerThan } from "../../../Utils/validations"
import ErrorPanel from "../FrameComponents/ErrorPanel"
import { makeQueryCreate } from "../../../Utils/baseMaps"
import { createObject } from "../../../Utils/apiQueries"

function CreateCoordinateSF({ onClose }: { onClose: () => void }) {
    const [x, setX] = useState("")
    const [y, setY] = useState("")
    const [errorMsgs, setErrorMsg] = useState<string[]>([])

    useEffect(() => {
        let res : string[] = []
        processIsValidFloat(x, false, "X", res)
        processIsLowerThan(x, 292, true, "X", res)
        processIsValidFloat(y, false, "Y", res)
        processIsBiggerThan(y, -244, true,"Y", res)
        setErrorMsg(res)
    }, [x, y]);

    function createQuery(){
        let form = new URLSearchParams()
        form.append('x', x)
        form.append('y', y)
        createObject(makeQueryCreate("Coordinate"),form)
        onClose()
    }

    return (
        <>
            <div className="modal-main-content">
              <div className="modal-header">Создание Coordinate</div>
                <div className="modal-params-container">
                    <ParameterField value={x} setValue={setX} required={true} type="floatNum" field="X" />
                    <ParameterField value={y} setValue={setY} required={true} type="floatNum" field="Y" />
                </div>
                <ErrorPanel errorMessages={errorMsgs} />  
            </div>
            <div className="modal-buttons">
                <button className="modal-close-button" onClick={onClose}>Закрыть</button>
                <button className={`${errorMsgs.length != 0 && "modal-button-disbled"} modal-save-button`} onClick={createQuery}>
                    Создать
                </button>
            </div>
        </>
    )
}

export default CreateCoordinateSF