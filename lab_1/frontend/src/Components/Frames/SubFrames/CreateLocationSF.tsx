import { useEffect, useState } from "react"
import ParameterField from "../FrameComponents/ParameterField"
import { processIsNotNull, processIsValidFloat, processStringMaxLength } from "../../../Utils/validations"
import ErrorPanel from "../FrameComponents/ErrorPanel"
import { makeQueryCreate, makeQueryDelete } from "../../../Utils/baseMaps"
import { createObject, removeObject } from "../../../Utils/apiQueries"
import type { Location } from "../../../interfaces/Entities/Location"
import BaseFrame from "../BaseFrame"
import ShowSF from "./ShowSF"
import DeleteAlert from "./DeleteAlert"

function CreateLocationSF({ onClose, id, location }: { onClose: () => void, id: number, location: Location|undefined }) {
    const [name, setName] : [any, React.Dispatch<any>] = useState("")
    const [x, setX] : [any, React.Dispatch<any>] = useState("")
    const [y, setY] : [any, React.Dispatch<any>] = useState("")
    const [z, setZ] : [any, React.Dispatch<any>] = useState("")
    const [alert, setAlert] = useState(false)
    const [errorMsgs, setErrorMsg] = useState<string[]>([])
    

    const [modalOpen, setModalOpen] = useState("")
    function showConnected(){
        setModalOpen("ShowPerson")
    }
    function modalOnClose(){
        setModalOpen("")
    }
    useEffect(() => {
        if(location != undefined){
            setName(location.name)
            setX(location.x?.toString())
            setY(location.y?.toString())
            if(location.z == null)  setZ("")
            else setZ(location.z?.toString())
        }
    },[])
    
    useEffect(() => {
        let res : string[] = []
        processStringMaxLength(name, 702, "Имя", res)
        processIsNotNull(name, "Имя", res)
        processIsValidFloat(x, false, "X", res)
        processIsValidFloat(y, false, "Y", res)
        processIsValidFloat(z, true, "Z", res)
        setErrorMsg(res)
    }, [name, x, y, z]);

    function createQuery(){
        let form = new URLSearchParams()
        if(id != -1) form.append('id', id.toString())
        form.append('name', name)
        form.append('x', x)
        form.append('y', y)
        form.append('z', z)
        createObject(makeQueryCreate("Location"),form)
        onClose()
    }

    function deleteQuery(){
        removeObject(makeQueryDelete("Location", id.toString()))
        .then(() =>onClose())
        .catch(() => {
            setAlert(true)
        })
    }
    
    return (
        <>
            <BaseFrame isOpen={alert} onClose={() => setAlert(false)} zindex={5000} width="30%" height="35%">
                <>
                    <DeleteAlert onClose={() => setAlert(false)}></DeleteAlert>
                </>
            </BaseFrame>
            <BaseFrame isOpen={modalOpen != ""} onClose={modalOnClose} zindex={1000} width="40%" height="">
                <>
                    {modalOpen=="ShowPerson" && <ShowSF onClose={modalOnClose} objectName="Person" filterColumnEx="locationId" filterDataEx={id.toString()}/>}
                </>
            </BaseFrame>
            <div className="modal-main-content">
              <div className="modal-header">{id == -1 ? "Создание" : "Изменение"} Location</div>
                <div className="modal-params-container">
                    <ParameterField value={name} setValue={setName} required={true} type="text" field="Имя" />
                    <ParameterField value={x} setValue={setX} required={true} type="floatNum" field="X" />
                    <ParameterField value={y} setValue={setY} required={true} type="floatNum" field="Y" />
                    <ParameterField value={z} setValue={setZ} required={false} type="floatNum" field="Z" />
                </div>
                <ErrorPanel errorMessages={errorMsgs} />  
            </div>
            <div className="modal-buttons">
                <button className="modal-close-button" onClick={onClose}>Закрыть</button>
                {
                    location != undefined && 
                    <button className={`modal-save-button`} onClick={showConnected}>
                        Связанные
                    </button>
                }
                {
                    location != undefined && 
                    <button className={`modal-save-button`} onClick={deleteQuery}>
                        Удалить
                    </button>
                }
                <button className={`${errorMsgs.length != 0 && "modal-button-disbled"} modal-save-button`} onClick={createQuery}>
                    {id == -1 ? "Создать" : "Изменить"}
                </button>
            </div>
        </>
    )
}

export default CreateLocationSF