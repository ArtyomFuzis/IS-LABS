import { useEffect, useState } from "react"
import ParameterField from "../FrameComponents/ParameterField"
import { processIsNotNull, processIsValidFloat } from "../../../Utils/validations"
import ErrorPanel from "../FrameComponents/ErrorPanel"
import { makeQueryCreate } from "../../../Utils/baseMaps"
import { createObject } from "../../../Utils/apiQueries"

function CreateLocationSF({ onClose }: { onClose: () => void }) {
    const [name, setName] = useState("")
    const [x, setX] = useState("")
    const [y, setY] = useState("")
    const [z, setZ] = useState("")
    const [errorMsgs, setErrorMsg] = useState<string[]>([])

    useEffect(() => {
        let res : string[] = []
        processIsNotNull(name, "Имя", res)
        processIsValidFloat(x, false, "X", res)
        processIsValidFloat(y, false, "Y", res)
        processIsValidFloat(z, true, "Z", res)
        setErrorMsg(res)
    }, [name, x, y, z]);

    function createQuery(){
        let form = new URLSearchParams()
        form.append('name', name)
        form.append('x', x)
        form.append('y', y)
        form.append('z', z)
        createObject(makeQueryCreate("Location"),form)
    }

    return (
        <>
            <div className="modal-main-content">
              <div className="modal-header">Создание Location</div>
                <div className="modal-params-container">
                    <ParameterField value={name} setValue={setName} required={true} type="text" field="Имя" />
                    <ParameterField value={x} setValue={setX} required={true} type="floatNum" field="X" />
                    <ParameterField value={y} setValue={setY} required={true} type="floatNum" field="Y" />
                    <ParameterField value={z} setValue={setZ} required={true} type="floatNum" field="Z" />
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

export default CreateLocationSF