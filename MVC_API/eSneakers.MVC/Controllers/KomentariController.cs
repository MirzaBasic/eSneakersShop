using eSneakers.DATA.Models;
using eSneakers.MVC.Models;
using eSneakers.WEB.Data;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;

namespace eSneakers.MVC.Controllers
{
    public class KomentariController : Controller
    {
        MojContext mc = new MojContext();
        // GET: Komentari
        public ActionResult Proizvod(int id)
        {
            var model = mc.Komentari.Where(x => x.ProizvodId == id).Select(x => new KomentarVM {

                Id = x.Id,
                Korisnik = x.Korisnik.Username,
                DatumKreiranja = x.DatumKreiranja.ToString(),
                ProizvodId=x.ProizvodId,
                Sadrzaj=x.Sadrzaj
            }).ToList();
            return Json(model, JsonRequestBehavior.AllowGet);
        }

        public ActionResult Dodaj(Komentar kom) {
            kom.DatumKreiranja = DateTime.Now;
            
            mc.Komentari.Add(kom);

            try
            {
                mc.SaveChanges();
            }
            catch
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }



            return new HttpStatusCodeResult(HttpStatusCode.OK);

        }
    }
}